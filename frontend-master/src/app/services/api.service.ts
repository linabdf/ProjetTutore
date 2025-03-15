import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  sendData(data: string): Observable<string> {
    const params = new HttpParams().set('data', data);
    return this.http.post(`${this.apiUrl}/sendData`, params, { responseType: 'text' });
  }
}
