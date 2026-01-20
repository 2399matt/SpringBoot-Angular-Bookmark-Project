import {Component} from '@angular/core';
import {AuthService} from "@auth0/auth0-angular";
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-login-status',
  standalone: true,
  imports: [
    RouterLink
  ],
  templateUrl: './login-status.component.html',
  styleUrl: './login-status.component.css'
})
export class LoginStatusComponent {

  username: string | null = null;
  isAuthenticated: boolean = false;

  constructor(private authService: AuthService) {

  }

  ngOnInit(): void {
    this.authService.isAuthenticated$.subscribe({
      next: auth => {
        this.isAuthenticated = auth;
        this.fetchUserDetails();
      }
    })
  }

  fetchUserDetails(): void {
    if (this.isAuthenticated) {
      this.authService.user$.subscribe({
        next: user => {
          if (user) {
            this.username = user.name ?? user.email ?? user.sub ?? '';
          }
        }
      })
    }
  }

  login(): void {
    this.authService.loginWithRedirect();
  }

  logout(): void {
    this.authService.logout();
  }
}
