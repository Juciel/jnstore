import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';
import { CaixaInput, CaixaRepresentation, PageCaixaRepresentation } from '../models';

@Injectable({ providedIn: 'root' })
export class CaixaService {
  private baseUrl = environment.apiUrl || 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  abrirCaixa(input: CaixaInput): Observable<CaixaRepresentation> {
    return this.http.post<CaixaRepresentation>(`${this.baseUrl}/api/caixas/abrir`, input);
  }

  fecharCaixa(id: number, input: CaixaInput): Observable<CaixaRepresentation> {
    return this.http.post<CaixaRepresentation>(`${this.baseUrl}/api/caixas/${id}/fechar`, input);
  }

  retiradaCaixa(id: number, input: CaixaInput): Observable<CaixaRepresentation> {
    return this.http.post<CaixaRepresentation>(`${this.baseUrl}/api/caixas/${id}/retirar`, input);
  }

  consultaCaixaAbertoHoje(): Observable<CaixaRepresentation | null> {
    return this.http.get<CaixaRepresentation | null>(`${this.baseUrl}/api/caixas/abrir`);
  }

  listarCaixas(): Observable<CaixaRepresentation[]> {
    return this.http.get<CaixaRepresentation[]>(`${this.baseUrl}/api/caixas`);
  }

  buscarPorId(id: number): Observable<CaixaRepresentation> {
    return this.http.get<CaixaRepresentation>(`${this.baseUrl}/api/caixas/${id}`);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/api/caixas/${id}`);
  }

  getAllPaginado(page: number, size: number, sort: string[], dataInicial?: string, dataFinal?: string): Observable<PageCaixaRepresentation> {
    let params = new HttpParams()
      .set('page', page)
      .set('size', size);

    if (dataInicial) {
      params = params.set('dataInicial', dataInicial);
    }
    if (dataFinal) {
      params = params.set('dataFinal', dataFinal);
    }

    sort.forEach(s => {
      params = params.append('sort', s);
    });

    return this.http.get<PageCaixaRepresentation>(`${this.baseUrl}/api/caixas/paginado`, { params });
  }
}
