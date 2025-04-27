import { Injectable, NgZone } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private notificationSubject = new Subject<string>();
  private notificationCountSubject = new Subject<number>();
  public notifications$ = this.notificationSubject.asObservable();
  public notificationCount$ = this.notificationCountSubject.asObservable(); // Observable du compteur

  constructor(private zone: NgZone) {}

  startSseConnection() {
    const eventSource = new EventSource('http://localhost:8080/notifications/stream');
    console.log("SSE connecté");

    eventSource.addEventListener('notif', (event: any) => {
      const data = JSON.parse(event.data);
      this.zone.run(() => {
        if (data.message) {
          this.notificationSubject.next(data.message);
        }   // Envoie le message
        this.notificationCountSubject.next(data.unreadCount);
        console.log("count",data.unreadCount)
      });
    });

    eventSource.onerror = (error) => {
      console.error("Erreur SSE :", error);
      eventSource.close();
    };
  }
  getUnreadCount() {
    const token = localStorage.getItem('token'); // Récupère le token JWT
    const headers = {
      'Authorization': `Bearer ${token}`
    };

    fetch('http://localhost:8080/notifications/unreadCount', {
      method: 'GET',
      headers: headers
    })
    .then(response => response.json())  // Convertir la réponse en JSON
    .then((count: number) => {
      this.notificationCountSubject.next(count);  // Mettre à jour le nombre de notifications non lues
    })
    .catch(error => {
      console.error("Erreur lors de la récupération des notifications non lues :", error);
    });
  }
  updateNotificationCount(count: number) {
    this.notificationCountSubject.next(count);
  }
  
}
