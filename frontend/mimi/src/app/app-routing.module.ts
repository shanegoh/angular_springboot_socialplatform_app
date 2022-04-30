import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdminHomeComponent } from './admin-home/admin-home.component';
import { IndividualPostComponent } from './individual-post/individual-post.component';
import { LoginComponent } from './login/login.component';
import { MainComponent } from './main/main.component';
import { ManageUserComponent } from './manage-user/manage-user.component';
import { RegisterComponent } from './register/register.component';
import { UserHomeComponent } from './user-home/user-home.component';
import { AuthGuard } from './_helpers/auth.guard';
import { Role } from './_models/role';

const routes: Routes = [
  { path: 'main',
    component: MainComponent
  },
  { path: 'user', 
    component: UserHomeComponent, 
    canActivate: [AuthGuard], 
    data: { roles: [Role.USER] } 
  },  
  { path: 'admin', 
    component: AdminHomeComponent, 
    canActivate: [AuthGuard], 
    data: { roles: [Role.ADMIN] } 
  },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  // { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {scrollPositionRestoration: 'enabled'})],
  exports: [RouterModule]
})
export class AppRoutingModule { }
