import { Routes } from '@angular/router';
import { LoginComponent } from '../feature/auth/login/login.component';
import { AuthGuard } from '../feature/services/auth/auth.guard'; // Importar AuthGuard

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
	{ path: '', pathMatch: 'full', redirectTo: 'dashboard' }, // Removido canActivate daqui
	{ path: 'dashboard', loadComponent: () => import('../feature/pages/dashboard/dashboard.component').then(m => m.DashboardComponent), canActivate: [AuthGuard] },
	{ path: 'produtos', loadComponent: () => import('../feature/pages/produto/produto-list/produto-list.component').then(m => m.ProdutoListComponent), canActivate: [AuthGuard] },
	{ path: 'produtos/novo', loadComponent: () => import('../feature/pages/produto/produto-form/produto-form.component').then(m => m.ProdutoFormComponent), canActivate: [AuthGuard] },
	{ path: 'produtos/:id/editar', loadComponent: () => import('../feature/pages/produto/produto-form/produto-form.component').then(m => m.ProdutoFormComponent), canActivate: [AuthGuard] },
	{ path: 'categorias', loadComponent: () => import('../feature/pages/categoria/categoria-list/categoria-list.component').then(m => m.CategoriaListComponent), canActivate: [AuthGuard] },
	{ path: 'categorias/nova', loadComponent: () => import('../feature/pages/categoria/categoria-form/categoria-form.component').then(m => m.CategoriaFormComponent), canActivate: [AuthGuard] },
	{ path: 'categorias/:id/editar', loadComponent: () => import('../feature/pages/categoria/categoria-form/categoria-form.component').then(m => m.CategoriaFormComponent), canActivate: [AuthGuard] },
	// Nova rota para a lista de caixas
	{ path: 'caixa', loadComponent: () => import('../feature/pages/caixa/caixa-list/caixa-list.component').then(m => m.CaixaListComponent), canActivate: [AuthGuard] },
	// Rota para abrir um caixa (mantida, mas agora acessível da lista)
	{ path: 'caixa/abrir', loadComponent: () => import('../feature/pages/caixa/caixa-open/caixa-open.component').then(m => m.CaixaOpenComponent), canActivate: [AuthGuard] },
	// Nova rota para fechar um caixa específico
	{ path: 'caixa/fechar/:id', loadComponent: () => import('../feature/pages/caixa/caixa-close/caixa-close.component').then(m => m.CaixaCloseComponent), canActivate: [AuthGuard] },
	{ path: 'caixa/retirada/:id', loadComponent: () => import('../feature/pages/caixa/caixa-retirada/caixa-retirada.component').then(m => m.CaixaRetiradaComponent), canActivate: [AuthGuard] },
	{ path: 'vendas', loadComponent: () => import('../feature/pages/venda/venda-list/venda-list.component').then(m => m.VendaListComponent), canActivate: [AuthGuard] },
	{ path: 'vendas/nova', loadComponent: () => import('../feature/pages/venda/venda-form/venda-form.component').then(m => m.VendaComponent), canActivate: [AuthGuard] }
];
