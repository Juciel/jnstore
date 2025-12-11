import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';
import { PageProdutoRepresentation, ProdutoRepresetation } from '../models';

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
  getAllPaginado(page: number, size: number, sort: string[], termo: string): Observable<PageProdutoRepresentation> {
    let params = new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('termo', termo);

    sort.forEach(s => {
      params = params.append('sort', s);
    });

    return this.http.get<PageProdutoRepresentation>(`${this.baseUrl}/produtos/paginado`, { params });
  }

  getProdutosBaixoEstoque(limit: number = 5): Observable<ProdutoRepresetation[]> {
    return this.http.get<ProdutoRepresetation[]>(`${this.baseUrl}/produtos/baixo-estoque?limit=${limit}`);
  }

  getProdutosEmFalta(limit: number = 5): Observable<ProdutoRepresetation[]> {
    return this.http.get<ProdutoRepresetation[]>(`${this.baseUrl}/produtos/em-falta?limit=${limit}`);
  }

}
