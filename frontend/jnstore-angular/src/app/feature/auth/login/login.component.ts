import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth/auth.service';
import { UsuarioService } from '../../services/usuario.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  loginForm!: FormGroup;
  loading = false;
  submitted = false;
  error = '';

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private authService: AuthService,
    private usuarioService: UsuarioService,
  ) { }

  ngOnInit() {
    this.loginForm = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  // convenience getter for easy access to form fields
  get f() { return this.loginForm.controls; }

  onSubmit() {
    this.submitted = true;

    // stop here if form is invalid
    if (this.loginForm.invalid) {
      return;
    }

    this.loading = true;
    const nomeUsuario = this.f['username'].value;
    this.authService.login({
      nomeUsuario: nomeUsuario,
      senha: this.f['password'].value
    })
      .subscribe({
        next: () => {
          this.usuarioService.isPrimeiroLogin(nomeUsuario).subscribe(isPrimeiro => {
            if (isPrimeiro) {
              this.router.navigate(['/atualizar-senha'], { state: { nomeUsuario: nomeUsuario } });
            } else {
              this.router.navigate(['/']); // Redireciona para a página inicial
            }
          });
        },
        error: err => {
          this.error = err.error && err.error.message ? err.error.message : err.message ? err.message : 'Ocorreu um erro desconhecido.';
          this.loading = false;
        }
      });
  }
}
