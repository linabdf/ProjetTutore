import { Injectable, NgZone } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private notificationSubject = new Subject<string>();
  public notifications$ = this.notificationSubject.asObservable();

  constructor(private zone: NgZone) {}

  startSseConnection() {
    const eventSource = new EventSource('http://localhost:8080/notifications/stream');
    console.log("SSE connecté");

    eventSource.addEventListener('notif', (event: any) => {
      const message = event.data;
      this.zone.run(() => {
        this.notificationSubject.next(message);
      });
    });

    eventSource.onerror = (error) => {
      console.error("Erreur SSE :", error);
      eventSource.close();
    };
  }
}
