import { Component, Inject, PLATFORM_ID, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule, isPlatformServer } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CategoriaService } from 'src/app/feature/services/categoria.service';
import { CategoriaRepresetation } from 'src/app/feature/models';

@Component({
  selector: 'app-categoria-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './categoria-form.component.html',
  styleUrls: ['./categoria-form.component.scss']
})
export class CategoriaFormComponent implements OnInit {
  form: any;
  loading = false;
  saveError: string | null = null;
  categorias: CategoriaRepresetation[] = [];

  isEditMode = false;
  categoriaId: number | null = null;

  constructor(private fb: FormBuilder, private categoriaService: CategoriaService,
              private route: ActivatedRoute, private router: Router,
              @Inject(PLATFORM_ID) private platformId: any,
              private cdr: ChangeDetectorRef) {
    this.form = this.fb.group({
      descricao: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]]
    });
  }

  ngOnInit(): void {
    if (!isPlatformServer(this.platformId)) {
      this.route.params.subscribe(params => {
        if (params['id']) {
          this.categoriaId = Number(params['id']);
          this.isEditMode = true;
          this.loadCategoria();
        }
      });
    }
  }

  loadCategoria(): void {
    if (!this.categoriaId) return;
    this.loading = true;
    this.categoriaService.buscarPorId(this.categoriaId).subscribe({
      next: (c) => {
        this.form.patchValue({ descricao: c.descricao });
        this.loading = false;
        try { this.cdr.detectChanges(); } catch (e) { /* ignore */ }
      },
      error: (e) => {
        this.saveError = 'Erro ao carregar categoria: ' + (e?.error?.message || e?.message || 'desconhecido');
        this.loading = false;
        try { this.cdr.detectChanges(); } catch (er) { /* ignore */ }
      }
    });
  }

  submit() {
    if (this.form.invalid) return;
    const raw: any = this.form.value;
    const categoria: CategoriaRepresetation = {
      descricao: raw.descricao ?? undefined
    };
    this.loading = true;
    this.saveError = null;
    try { this.cdr.detectChanges(); } catch (e) { /* ignore */ }

    const saveObs = this.isEditMode && this.categoriaId
      ? this.categoriaService.atualizar(this.categoriaId, categoria)
      : this.categoriaService.criar(categoria);

    saveObs.subscribe({
      next: () => {
        this.router.navigate(['/categorias'], { state: { successMessage: 'Categoria salva com sucesso!' } });
      },
      error: (err) => {
        this.saveError = err?.error?.message || err?.message || 'Erro ao salvar categoria';
        this.loading = false;
        try { this.cdr.detectChanges(); } catch (e) { /* ignore */ }
      }
    });
  }

}
