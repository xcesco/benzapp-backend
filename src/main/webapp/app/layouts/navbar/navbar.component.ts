import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { SessionStorageService } from 'ngx-webstorage';

import { VERSION } from 'app/app.constants';
import { LANGUAGES } from 'app/config/language.constants';
import { AccountService } from 'app/core/auth/account.service';
import { LoginService } from 'app/login/login.service';
import { ProfileService } from 'app/layouts/profiles/profile.service';
import { EMPTY, Observable, of } from 'rxjs';
import { Account } from 'app/core/auth/account.model';
import { GestoreService } from 'app/entities/gestore/service/gestore.service';
import { CittadinoService } from 'app/entities/cittadino/service/cittadino.service';
import { mergeMap } from 'rxjs/operators';
import { HttpResponse } from '@angular/common/http';
import { Cittadino, ICittadino } from 'app/entities/cittadino/cittadino.model';

@Component({
  selector: 'jhi-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
})
export class NavbarComponent implements OnInit {
  inProduction?: boolean;
  isNavbarCollapsed = true;
  languages = LANGUAGES;
  openAPIEnabled?: boolean;
  account: Account | null = null;
  accountAuthenticated = false;

  gestoreId: number | undefined | null = null;
  userId: number | undefined | null = null;

  version: string;

  constructor(
    private loginService: LoginService,
    private translateService: TranslateService,
    private sessionStorage: SessionStorageService,
    private accountService: AccountService,
    private profileService: ProfileService,
    private gestoreService: GestoreService,
    private cittadinoService: CittadinoService,
    private router: Router
  ) {
    this.version = VERSION ? (VERSION.toLowerCase().startsWith('v') ? VERSION : 'v' + VERSION) : '';
  }

  ngOnInit(): void {
    this.profileService.getProfileInfo().subscribe(profileInfo => {
      this.inProduction = profileInfo.inProduction;
      this.openAPIEnabled = profileInfo.openAPIEnabled;
    });

    this.accountService.getAuthenticationState().subscribe(account => {
      this.account = account;
      this.accountAuthenticated = true;
      this.userId = null;
      this.gestoreId = null;

      if (account != null) {
        if (this.hasAuthority(['ROLE_PATROL_STATION'])) {
          this.gestoreService.queryByOwner(account.login).subscribe(value => {
            this.gestoreId = value.body?.[0]?.id;
            this.account = account;
            this.accountAuthenticated = true;
          });
        } else if (this.hasAuthority(['ROLE_USER'])) {
          this.cittadinoService.queryByOwner(account.login).subscribe(value => {
            this.userId = value.body?.[0]?.id;
            this.account = account;
            this.accountAuthenticated = true;
          });
        } else {
          this.account = account;
          this.accountAuthenticated = true;
        }
      } else {
        this.account = null;
        this.accountAuthenticated = false;
      }
    });
  }

  changeLanguage(languageKey: string): void {
    this.sessionStorage.store('locale', languageKey);
    this.translateService.use(languageKey);
  }

  collapseNavbar(): void {
    this.isNavbarCollapsed = true;
  }

  isAuthenticated(): boolean {
    return this.accountService.isAuthenticated();
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  logout(): void {
    this.collapseNavbar();
    this.loginService.logout();
    this.router.navigate(['']);
  }

  toggleNavbar(): void {
    this.isNavbarCollapsed = !this.isNavbarCollapsed;
  }

  getImageUrl(): string {
    return this.isAuthenticated() ? this.accountService.getImageUrl() : '';
  }

  hasAuthority(authorities: string[]): boolean {
    return this.accountService.hasAnyAuthority(authorities);
  }
}
