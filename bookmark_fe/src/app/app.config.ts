import {ApplicationConfig} from '@angular/core';
import {provideRouter} from '@angular/router';

import {routes} from './app.routes';
import {authHttpInterceptorFn, provideAuth0} from "@auth0/auth0-angular";
import {environment} from "../environments/environment";
import {provideHttpClient, withInterceptors} from "@angular/common/http";

export const appConfig: ApplicationConfig = {
  providers: [provideRouter(routes), provideAuth0({
    domain: environment.auth0Domain,
    clientId: environment.auth0ClientId,
    useRefreshTokens: true,
    cacheLocation: 'localstorage',
    authorizationParams: {
      redirect_uri: environment.auth0Redirect,
      audience: environment.auth0Aud
    },
    httpInterceptor: {
      allowedList: [
        {
          uri: environment.interceptorUrl,
          tokenOptions: {
            authorizationParams: {
              audience: environment.auth0Aud,
              scope: 'openid email profile'
            }
          }

        }
      ]
    }
  }),
    provideHttpClient(withInterceptors([authHttpInterceptorFn]))
  ]
};
