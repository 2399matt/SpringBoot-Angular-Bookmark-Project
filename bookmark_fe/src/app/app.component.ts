import {Component} from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {LoginStatusComponent} from "./components/login-status/login-status.component";
import {SideBarComponent} from "./components/side-bar/side-bar.component";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, LoginStatusComponent, SideBarComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'bookmark_fe';
}
