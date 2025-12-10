import { BootstrapContext, bootstrapApplication } from '@angular/platform-browser';
import { App } from '../app/feature/app';
import { config } from '../app/config/app.config.server';

const bootstrap = (context: BootstrapContext) =>
    bootstrapApplication(App, config, context);

export default bootstrap;
