import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { UsuarioService } from '../../services/usuario.service';

@Component({
  selector: 'app-atualizar-senha',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  templateUrl: './atualizar-senha.component.html',
  styleUrls: ['./atualizar-senha.component.css']
})
export class AtualizarSenhaComponent implements OnInit {
  atualizarSenhaForm!: FormGroup;
  loading = false;
  submitted = false;
  error = '';
  success = '';
  nomeUsuario: string;

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private usuarioService: UsuarioService
  ) {
    const navigation = this.router.getCurrentNavigation();
    this.nomeUsuario = navigation?.extras?.state?.['nomeUsuario'];

    if (!this.nomeUsuario) {
      this.router.navigate(['/login']); // Redireciona se não houver nome de usuário
    }
  }

  ngOnInit() {
    this.atualizarSenhaForm = this.formBuilder.group({
      novaSenha: ['', [Validators.required, Validators.minLength(6)]],
      confirmarSenha: ['', Validators.required]
    }, {
      validator: this.mustMatch('novaSenha', 'confirmarSenha')
    });
  }

  // convenience getter for easy access to form fields
  get f() { return this.atualizarSenhaForm.controls; }

  onSubmit() {
    this.submitted = true;

    // stop here if form is invalid
    if (this.atualizarSenhaForm.invalid) {
      return;
    }

    this.loading = true;
    const request = {
      nomeUsuario: this.nomeUsuario,
      novaSenha: this.f['novaSenha'].value
    };

    this.usuarioService.atualizarSenha(request)
      .subscribe({
        next: () => {
          this.success = 'Senha atualizada com sucesso! Redirecionando para o login...';
          setTimeout(() => {
            this.router.navigate(['/login']);
          }, 3000);
        },
        error: err => {
          this.error = err.error && err.error.message ? err.error.message : 'Ocorreu um erro desconhecido.';
          this.loading = false;
        }
      });
  }

  mustMatch(controlName: string, matchingControlName: string) {
    return (formGroup: FormGroup) => {
      const control = formGroup.controls[controlName];
      const matchingControl = formGroup.controls[matchingControlName];

      if (matchingControl.errors && !matchingControl.errors['mustMatch']) {
        // return if another validator has already found an error on the matchingControl
        return;
      }

      // set error on matchingControl if validation fails
      if (control.value !== matchingControl.value) {
        matchingControl.setErrors({ mustMatch: true });
      } else {
        matchingControl.setErrors(null);
      }
    }
  }
}
