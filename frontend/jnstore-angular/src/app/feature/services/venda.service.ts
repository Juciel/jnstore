import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';
import { VendaInput, VendaRepresentation } from '../models';

@Injectable({ providedIn: 'root' })
export class VendaService {
  private baseUrl = environment.apiUrl || 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  registrarVenda(input: VendaInput): Observable<VendaRepresentation> {
    return this.http.post<VendaRepresentation>(`${this.baseUrl}/vendas`, input);
  }

  listarVendas(): Observable<VendaRepresentation[]> {
    return this.http.get<VendaRepresentation[]>(`${this.baseUrl}/vendas`);
  }

  buscarPorId(id: number): Observable<VendaRepresentation> {
    return this.http.get<VendaRepresentation>(`${this.baseUrl}/vendas/${id}`);
  }

  deletarVenda(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/vendas/${id}`);
  }
}
