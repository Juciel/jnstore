import { Component, signal, inject } from '@angular/core';
import { RouterOutlet, RouterLink, RouterLinkActive, Router, NavigationEnd } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from './services/auth/auth.service';
import { UsuarioRepresentation } from './models/usuario.model'; // Caminho corrigido para UsuarioRepresentation

@Component({
  selector: 'app-root',
  imports: [
    RouterOutlet,
    RouterLink,
    RouterLinkActive,
    CommonModule,
  ],
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
  protected readonly isLoginPage = signal(false);
  protected readonly usuarioLogado = signal<UsuarioRepresentation | null>(null);

  private readonly router = inject(Router);
  private readonly authService = inject(AuthService);

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
      '/caixa/retirada/:id': 'Retirar Valor do Caixa',
      '/vendas': 'Vendas',
      '/vendas/nova': 'Registrar Venda'
    };

    this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        const url = event.urlAfterRedirects.split('?')[0];
        this.isLoginPage.set(url === '/login');

        if (!this.isLoginPage() && this.authService.isLoggedIn() && !this.usuarioLogado()) {
          this.authService.getUsuarioLogado().subscribe({
            next: (user) => {
              this.usuarioLogado.set(user);
            },
            error: (err) => {
              console.error('Erro ao buscar usuário logado:', err);
              this.authService.logout();
              this.router.navigate(['/login']);
            }
          });
        } else if (this.isLoginPage() || !this.authService.isLoggedIn()) {
          this.usuarioLogado.set(null);
        }

        let matchedTitle: string | undefined;
        matchedTitle = titleMap[url];

        if (!matchedTitle) {
          for (const key in titleMap) {
            if (key.includes(':')) {
              const staticPart = key.split(':')[0];
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

  protected logout(): void {
    this.authService.logout();
    this.usuarioLogado.set(null);
    this.router.navigate(['/login']);
  }

  private humanizeUrl(url: string): string {
    if (!url || url === '/' ) return 'Dashboard';
    const parts = url.replace(/^\//, '').split('/');
    const last = parts[parts.length - 1];
    return last.replace(/-|_/g, ' ').replace(/\b\w/g, s => s.toUpperCase());
  }
}
