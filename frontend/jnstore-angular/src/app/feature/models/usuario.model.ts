export interface UsuarioRepresentation {
  id?: number;
  nome?: string;
  nomeUsuario?: string;
  email?: string;
  perfis?: PerfilRepresentation[];
}

export interface AtualizarSenhaRequest {
  nomeUsuario?: string;
  novaSenha?: string;
}

export interface PerfilRepresentation {
  id?: number;
  nome?: string;
}
