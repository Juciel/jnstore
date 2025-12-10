import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from '../app/config/app.config';
import { App } from '../app/feature/app';
import { registerLocaleData } from '@angular/common';
import localePt from '@angular/common/locales/pt';

registerLocaleData(localePt);

bootstrapApplication(App, appConfig)
  .catch((err) => console.error(err));
