import { Component, EventEmitter, Input, Output, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserService } from 'src/app/services/user/user.service';

@Component({
  selector: 'app-formulario-crear-usuario',
  templateUrl: './formulario-crear-usuario.component.html',
  styleUrls: ['./formulario-crear-usuario.component.scss']
})
export class FormularioCrearUsuarioComponent implements OnInit {

  @Input() mostrarFormulario: boolean = false; // Controla la visibilidad del formulario
  @Output() onClose = new EventEmitter<void>();
  @Output() onUsuarioGuardado = new EventEmitter<void>();

  usuarioForm: FormGroup = new FormGroup({}); // Default value

  loginError: string = '';

  constructor(private formBuilder: FormBuilder, private usuarioService: UserService) {}

  ngOnInit(): void {
    this.initForm();
  }

  initForm() {
    this.usuarioForm = this.formBuilder.group({
      firstname: ['', Validators.required],
      lastname: ['', Validators.required],
      username: ['', [Validators.required, Validators.email]], // Validar que sea un correo
      password: ['', Validators.required],
      country: [''],
      role: ['ADMIN']
    });
  }

  guardarUsuario() {
    if (this.usuarioForm.valid) {
      this.usuarioService.agregarUser(this.usuarioForm.value).subscribe(
        response => {
          console.log('Usuario guardado:', response);
          this.resetFormulario();
          this.cerrarFormulario(); // Cierra el formulario después de guardar
        },
        error => {
          console.error('Error al guardar el usuario:', error);
          this.loginError = 'Error al guardar el usuario. Por favor, inténtelo de nuevo.';
        }
      );
    } else {
      this.usuarioForm.markAllAsTouched(); // Marca todos los campos como tocados para mostrar errores
    }
  }

  cerrarFormulario() {
    this.resetFormulario();
    this.mostrarFormulario = false; // Cambia el valor de mostrarFormulario para cerrar el formulario
    this.onClose.emit();
  }

  resetFormulario() {
    this.usuarioForm.reset({
      firstname: '',
      lastname: '',
      username: '',
      password: '',
      country: '',
      role: 'ADMIN'
    });
    this.loginError = ''; // Limpia el mensaje de error
  }

  onFormClick(event: MouseEvent) {
    // Evitar que el clic en el formulario cierre el formulario
    event.stopPropagation();
  }

  // Métodos getter para los controles del formulario
  get firstname() {
    return this.usuarioForm.get('firstname');
  }

  get lastname() {
    return this.usuarioForm.get('lastname');
  }

  get username() {
    return this.usuarioForm.get('username');
  }

  get password() {
    return this.usuarioForm.get('password');
  }

  get country() {
    return this.usuarioForm.get('country');
  }

  get role() {
    return this.usuarioForm.get('role');
  }
}
