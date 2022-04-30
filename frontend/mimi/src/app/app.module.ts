import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap'; 
import { LoginComponent } from './login/login.component';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { JwtHelperService, JWT_OPTIONS  } from '@auth0/angular-jwt';
import { UserHomeComponent } from './user-home/user-home.component';
import { MainComponent } from './main/main.component';
import { RegisterComponent } from './register/register.component';
import { PopupComponent } from './popup/popup.component';
import { FeedComponent } from './feed/feed.component';
import { JwtInterceptor } from './_helpers/jwt.interceptor';
import { ToastComponent } from './toast/toast.component';
import { SafePipe } from './safe.pipe';
import { NAVComponent } from './nav/nav.component';
import { AddPostComponent } from './add-post/add-post.component';
import { PostFormComponent } from './post-form/post-form.component';
import { NotificationComponent } from './notification/notification.component';
import { IndividualPostComponent } from './individual-post/individual-post.component';
import { UpdateFormComponent } from './update-form/update-form.component';
import { AdminHomeComponent } from './admin-home/admin-home.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    UserHomeComponent,
    MainComponent,
    RegisterComponent,
    PopupComponent,
    FeedComponent,
    ToastComponent,
    SafePipe,
    NAVComponent,
    AddPostComponent,
    PostFormComponent,
    NotificationComponent,
    IndividualPostComponent,
    UpdateFormComponent,
    AdminHomeComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
  ],
  providers: [
    { provide: JWT_OPTIONS, useValue: JWT_OPTIONS },
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
    JwtHelperService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
