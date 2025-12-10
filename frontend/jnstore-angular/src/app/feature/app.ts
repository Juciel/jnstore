import { Component, signal, inject } from '@angular/core';
import { RouterOutlet, RouterLink, RouterLinkActive, Router, NavigationEnd } from '@angular/router';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class App {
  protected readonly title = signal('jnstore-angular');

  // controls sidebar visibility; true = visible
  protected readonly sidebarVisible = signal(true);

  protected toggleSidebar(): void {
    this.sidebarVisible.update(v => !v);
  }

  // dynamic page title based on current route
  protected readonly pageTitle = signal('Dashboard');

  private readonly router = inject(Router);

  constructor() {
    // map specific routes to friendly titles
    const titleMap: Record<string, string> = {
      '/dashboard': 'Dashboard',
      '/produtos': 'Produtos',
      '/produtos/novo': 'Cadastrar Produto',
      '/categorias': 'Categorias',
      '/categorias/nova': 'Cadastrar Categoria',
      '/caixa': 'Caixa',
      '/caixa/abrir': 'Abrir Caixa',
      '/caixa/fechar/:id': 'Fechar Caixa',
      '/vendas': 'Vendas',
      '/vendas/nova': 'Registrar Venda'
    };

    this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        const url = event.urlAfterRedirects.split('?')[0];
        let matchedTitle: string | undefined;

        // Tenta encontrar uma correspondência exata primeiro
        matchedTitle = titleMap[url];

        // Se não houver correspondência exata, tenta corresponder rotas dinâmicas
        if (!matchedTitle) {
          for (const key in titleMap) {
            if (key.includes(':')) { // Verifica se é uma rota dinâmica
              const staticPart = key.split(':')[0]; // Ex: '/caixa/fechar/'
              if (url.startsWith(staticPart)) {
                matchedTitle = titleMap[key];
                break;
              }
            }
          }
        }

        this.pageTitle.set(matchedTitle ?? this.humanizeUrl(url) ?? 'Dashboard');
      }
    });
  }

  private humanizeUrl(url: string): string {
    if (!url || url === '/' ) return 'Dashboard';
    const parts = url.replace(/^\//, '').split('/');
    // pick last significant part
    const last = parts[parts.length - 1];
    return last.replace(/-|_/g, ' ').replace(/\b\w/g, s => s.toUpperCase());
  }
}
