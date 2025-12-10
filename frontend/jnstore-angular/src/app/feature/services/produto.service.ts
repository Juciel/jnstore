import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';
import { ProdutoRepresetation } from '../models';

@Injectable({ providedIn: 'root' })
export class ProdutoService {
  private baseUrl = environment.apiUrl || 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  getAll(): Observable<ProdutoRepresetation[]> {
    return this.http.get<ProdutoRepresetation[]>(`${this.baseUrl}/produtos`);
  }

  getById(id: number): Observable<ProdutoRepresetation> {
    return this.http.get<ProdutoRepresetation>(`${this.baseUrl}/produtos/${id}`);
  }

  create(produto: ProdutoRepresetation): Observable<ProdutoRepresetation> {
    return this.http.post<ProdutoRepresetation>(`${this.baseUrl}/produtos`, produto);
  }

  update(id: number, produto: ProdutoRepresetation): Observable<ProdutoRepresetation> {
    return this.http.put<ProdutoRepresetation>(`${this.baseUrl}/produtos/${id}`, produto);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/produtos/${id}`);
  }

  /**
   * Busca um novo SKU para uma variação de produto.
   */
  getNewSku(): Observable<string> {
    return this.http.get(`${this.baseUrl}/produtos/new-sku`, { responseType: 'text' });
  }

  /**
   * Busca produtos para autocomplete.
   * @param termo Termo de busca para o autocomplete (SKU, nome ou categoria)
   */
  autocomplete(termo: string): Observable<ProdutoRepresetation[]> {
    const params = new HttpParams().set('termo', termo);
    return this.http.get<ProdutoRepresetation[]>(`${this.baseUrl}/produtos/autocomplete`, { params });
  }
}
