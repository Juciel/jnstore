export interface UsuarioRepresentation {
  id?: number;
  nome?: string;
  nomeUsuario?: string;
  email?: string;
  perfis?: PerfilRepresentation[];
}

export interface PerfilRepresentation {
  id?: number;
  nome?: string;
}
