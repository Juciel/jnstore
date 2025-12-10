import { Routes } from '@angular/router';

export const routes: Routes = [
	{ path: '', pathMatch: 'full', redirectTo: 'dashboard' },
	{ path: 'dashboard', loadComponent: () => import('../feature/pages/dashboard/dashboard.component').then(m => m.DashboardComponent) },
	{ path: 'produtos', loadComponent: () => import('../feature/pages/produto/produto-list.component').then(m => m.ProdutoListComponent) },
	{ path: 'produtos/novo', loadComponent: () => import('../feature/pages/produto/produto-form.component').then(m => m.ProdutoFormComponent) },
	{ path: 'produtos/:id/editar', loadComponent: () => import('../feature/pages/produto/produto-form.component').then(m => m.ProdutoFormComponent) },
	{ path: 'categorias', loadComponent: () => import('../feature/pages/categoria/categoria-list.component').then(m => m.CategoriaListComponent) },
	{ path: 'categorias/nova', loadComponent: () => import('../feature/pages/categoria/categoria-form.component').then(m => m.CategoriaFormComponent) },
	{ path: 'categorias/:id/editar', loadComponent: () => import('../feature/pages/categoria/categoria-form.component').then(m => m.CategoriaFormComponent) },
	// Nova rota para a lista de caixas
	{ path: 'caixa', loadComponent: () => import('../feature/pages/caixa/caixa-list.component').then(m => m.CaixaListComponent) },
	// Rota para abrir um caixa (mantida, mas agora acessível da lista)
	{ path: 'caixa/abrir', loadComponent: () => import('../feature/pages/caixa/caixa-open.component').then(m => m.CaixaOpenComponent) },
	// Nova rota para fechar um caixa específico
	{ path: 'caixa/fechar/:id', loadComponent: () => import('../feature/pages/caixa/caixa-close.component').then(m => m.CaixaCloseComponent) },
	{ path: 'vendas', loadComponent: () => import('../feature/pages/venda/venda-list.component').then(m => m.VendaListComponent) },
	{ path: 'vendas/nova', loadComponent: () => import('../feature/pages/venda/venda.component').then(m => m.VendaComponent) }
];
