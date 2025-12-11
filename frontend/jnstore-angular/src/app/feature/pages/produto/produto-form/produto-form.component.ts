import { Component, Inject, PLATFORM_ID, OnInit, ChangeDetectorRef, ViewChild } from '@angular/core';
import { CommonModule, isPlatformServer } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, Validators, FormArray, AbstractControl } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ProdutoService } from 'src/app/feature/services/produto.service';
import { CategoriaService } from 'src/app/feature/services/categoria.service';
import { CategoriaRepresetation, ProdutoRepresetation } from 'src/app/feature/models';
import { CurrencyMaskDirective } from 'src/app/feature/directives/currency-mask.directive';
import { LoadingService } from 'src/app/feature/services/loading.service';
import { delay, finalize, of } from 'rxjs';
import { VariacaoModalComponent } from 'src/app/feature/pages/produto/variacoes/variacao-modal.component';
import { VariacaoTabelaComponent } from 'src/app/feature/pages/produto/variacoes/variacao-tabela.component';

@Component({
  selector: 'app-produto-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink, CurrencyMaskDirective, VariacaoModalComponent, VariacaoTabelaComponent],
  templateUrl: './produto-form.component.html',
  styleUrls: ['./produto-form.component.scss']
})
export class ProdutoFormComponent implements OnInit {
  form: any;

  categorias: CategoriaRepresetation[] = [];
  loadingCategorias = false;
  categoriasError: string | null = null;
  saving = false;
  saveError: string | null = null;

  isEditMode = false;
  produtoId: number | null = null;
  loadingProduto = false;

  @ViewChild(VariacaoModalComponent) variacaoModal!: VariacaoModalComponent;

  constructor(private fb: FormBuilder, private produtoService: ProdutoService, private categoriaService: CategoriaService, private loadingService: LoadingService,
              private route: ActivatedRoute, private router: Router,
              @Inject(PLATFORM_ID) private platformId: any,
              private cdr: ChangeDetectorRef) {
    this.form = this.fb.group({
      nome: ['', Validators.required],
      descricao: [''],
      valorCompra: ['', Validators.required],
      valorVenda: ['', Validators.required],
      categoriaId: ['', Validators.required],
      genero: ['', Validators.required],
      variacoes: this.fb.array([], Validators.minLength(1))
    });
  }

  ngOnInit(): void {
    if (!isPlatformServer(this.platformId)) {

      this.loadCategorias();
      this.route.params.subscribe(params => {
        if (params['id']) {
          this.produtoId = Number(params['id']);
          this.isEditMode = true;
          this.loadProduto();
        }
      });
    }
  }

  loadProduto(): void {
    if (!this.produtoId) return;
    this.loadingProduto = true;
    this.produtoService.getById(this.produtoId).subscribe({
      next: (p) => {
        this.form.patchValue({
          nome: p.nome,
          descricao: p.descricao,
          valorVenda: p.valorVenda,
          valorCompra: p.valorCompra,
          categoriaId: p.categoria?.id,
          genero: p.genero
        });
        // populate variacoes if available
        if (p.variacoes && p.variacoes.length) {
          while (this.variacoes.length) this.variacoes.removeAt(0);
          p.variacoes.forEach(v => {
            this.variacoes.push(this.fb.group({
              id: [v.id],
              identificador: [v.identificador],
              cor: [v.cor],
              tamanho: [v.tamanho],
              quantidadeEstoque: [v.quantidadeEstoque, Validators.required]
            }));
          });
        }
        this.loadingProduto = false;
        try { this.cdr.detectChanges(); } catch (e) { /* ignore */ }
      },
      error: (e) => {
        this.saveError = 'Erro ao carregar produto: ' + (e?.error?.message || e?.message || 'desconhecido');
        this.loadingProduto = false;
        try { this.cdr.detectChanges(); } catch (er) { /* ignore */ }
      }
    });
  }

  get variacoes() {
    return this.form.get('variacoes') as FormArray;
  }

  openVariacaoModal(index?: number) {
    const variacao = index !== undefined ? this.variacoes.at(index) : undefined;
    this.variacaoModal.open(index, variacao);
  }

  onVariacaoSave(event: { index: number | null, data: any }) {
    if (event.index !== null && event.index >= 0) {
      // Editando variação existente
      this.variacoes.at(event.index).patchValue(event.data);
    } else {
      // Adicionando nova variação
      this.variacoes.push(this.fb.group({
        ...event.data,
        // Garante que a validação seja adicionada ao novo grupo
        quantidadeEstoque: [event.data.quantidadeEstoque, [Validators.required, Validators.min(0)]]
      }));
    }
    this.cdr.detectChanges();
  }


  removeVariacao(i: number) {
    this.variacoes.removeAt(i);
  }

  submit() {
    const raw: any = this.form.value;
    const produto: ProdutoRepresetation = {
      nome: raw.nome ?? undefined,
      descricao: raw.descricao ?? undefined,
      valorCompra: raw.valorCompra ?? undefined,
      valorVenda: raw.valorVenda ?? undefined,
      genero: raw.genero ?? undefined,
      categoria: raw.categoriaId ? { id: Number(raw.categoriaId), descricao: '' } : undefined,
      variacoes: (raw.variacoes || []).map((v: any) => ({
        id: v.id,
        identificador: v.identificador ?? undefined,
        cor: v.cor ?? undefined,
        tamanho: v.tamanho ?? undefined,
        quantidadeEstoque: v.quantidadeEstoque ?? undefined
      }))
    };
    this.saving = true;
    this.saveError = null;
    try { this.cdr.detectChanges(); } catch (e) { /* ignore */ }

    const saveObs = this.isEditMode && this.produtoId
      ? this.produtoService.update(this.produtoId, produto)
      : this.produtoService.create(produto);

    saveObs.subscribe({
      next: () => {
        this.router.navigate(['/produtos'], { state: { successMessage: 'Produto salvo com sucesso!' } });
      },
      error: (err) => {
        this.saveError = err?.error?.message || err?.message || 'Erro ao salvar produto';
        this.saving = false;
        try { this.cdr.detectChanges(); } catch (e) { /* ignore */ }
      }
    });
  }

  loadCategorias() {
    this.loadingCategorias = true;
    this.loadingService.show();
    this.categoriasError = null;
    this.categoriaService.listarTodas().pipe(
      finalize(() => {
        this.loadingCategorias = false;
        if (!this.isEditMode) this.loadingService.hide(); // Esconde o loading se não estiver carregando um produto
      })
    ).subscribe({ next: res => { this.categorias = res || []; try { this.cdr.detectChanges(); } catch (e) { /* ignore */ } }, error: e => { this.categoriasError = 'Erro ao carregar categoria: ' + (e?.error?.message || e?.message || 'desconhecido'); try { this.cdr.detectChanges(); } catch (er) { /* ignore */ } } });
  }
}
