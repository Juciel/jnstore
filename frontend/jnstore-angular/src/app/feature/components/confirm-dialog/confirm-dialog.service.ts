import { Injectable, Injector, ComponentRef, EnvironmentInjector, createComponent } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { ConfirmDialogComponent } from './confirm-dialog.component';

export interface ConfirmOptions {
  title?: string;
  message?: string;
  okText?: string;
  cancelText?: string;
  isDangerous?: boolean;
}

@Injectable({ providedIn: 'root' })
export class ConfirmDialogService {
  constructor(private injector: EnvironmentInjector) {}

  open(options: ConfirmOptions): Observable<boolean> {
    const subject = new Subject<boolean>();

    // Create container
    const container = document.createElement('div');
    document.body.appendChild(container);

    // Create component using newer Angular API
    const componentRef = createComponent(ConfirmDialogComponent, {
      environmentInjector: this.injector,
      hostElement: container
    });

    const instance = componentRef.instance;

    // Set inputs
    if (options.title) instance.title = options.title;
    if (options.message) instance.message = options.message;
    if (options.okText) instance.okText = options.okText;
    if (options.cancelText) instance.cancelText = options.cancelText;
    if (options.isDangerous !== undefined) instance.isDangerous = options.isDangerous;

    // Handle events
    const confirmSub = instance.confirmed.subscribe(() => {
      subject.next(true);
      subject.complete();
      this.cleanup(componentRef, container);
      confirmSub.unsubscribe();
      cancelSub.unsubscribe();
    });

    const cancelSub = instance.cancelled.subscribe(() => {
      subject.next(false);
      subject.complete();
      this.cleanup(componentRef, container);
      confirmSub.unsubscribe();
      cancelSub.unsubscribe();
    });

    componentRef.changeDetectorRef.detectChanges();

    return subject.asObservable();
  }

  private cleanup(componentRef: ComponentRef<ConfirmDialogComponent>, container: HTMLElement): void {
    componentRef.destroy();
    if (container.parentNode) {
      container.parentNode.removeChild(container);
    }
  }
}
