import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-sidenav',
  standalone: true,
  imports: [RouterModule, MatListModule, MatIconModule],
  templateUrl: './sidenav.component.html',
  styleUrls: ['./sidenav.component.scss']
})
export class SidenavComponent {
  menuItems = [
    { path: '/dashboard', icon: 'dashboard', label: 'Dashboard' },
    { path: '/tasks', icon: 'list', label: 'Tarefas' }
  ];
}