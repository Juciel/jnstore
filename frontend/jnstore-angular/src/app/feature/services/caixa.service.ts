import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';
import { CaixaInput, CaixaRepresentation } from '../models';

@Injectable({ providedIn: 'root' })
export class CaixaService {
  private baseUrl = environment.apiUrl || 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  abrirCaixa(input: CaixaInput): Observable<CaixaRepresentation> {
    return this.http.post<CaixaRepresentation>(`${this.baseUrl}/caixas/abrir`, input);
  }

  fecharCaixa(id: number, input: CaixaInput): Observable<CaixaRepresentation> {
    return this.http.post<CaixaRepresentation>(`${this.baseUrl}/caixas/${id}/fechar`, input);
  }

  consultaCaixaAbertoHoje(): Observable<CaixaRepresentation | null> {
    return this.http.get<CaixaRepresentation | null>(`${this.baseUrl}/caixas/abrir`);
  }

  listarCaixas(): Observable<CaixaRepresentation[]> {
    return this.http.get<CaixaRepresentation[]>(`${this.baseUrl}/caixas`);
  }

  buscarPorId(id: number): Observable<CaixaRepresentation> {
    return this.http.get<CaixaRepresentation>(`${this.baseUrl}/caixas/${id}`);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/caixas/${id}`);
  }
}
