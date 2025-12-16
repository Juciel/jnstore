import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';
import { TaxaInput, TaxaRepresentation } from '../models';

@Injectable({ providedIn: 'root' })
export class TaxaService {
  private baseUrl = environment.apiUrl || 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  getPorNome(nome: string): Observable<TaxaRepresentation> {
    let params = new HttpParams().set('nome', nome)
    return this.http.get<TaxaRepresentation>(`${this.baseUrl}/api/taxas`, { params });
  }

  getPorId(id: number): Observable<TaxaRepresentation> {
    return this.http.get<TaxaRepresentation>(`${this.baseUrl}/api/taxas/${id}`);
  }

  create(produto: TaxaInput): Observable<TaxaRepresentation> {
      return this.http.post<TaxaRepresentation>(`${this.baseUrl}/api/taxas`, produto);
  }

  update(id: number, produto: TaxaInput): Observable<TaxaRepresentation> {
    return this.http.put<TaxaRepresentation>(`${this.baseUrl}/api/taxas/${id}`, produto);
  }
}
