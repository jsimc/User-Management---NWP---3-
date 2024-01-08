import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, Router, CanActivateFn } from '@angular/router';

export const canActivate: CanActivateFn = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot) => {
  const token = sessionStorage.getItem('jwt');
  const router = inject(Router);

  // probaj da pozoves neku fju servis i ako puca jbggg
  if (token) {
    // ovde bi mogla da dodas za Create/ Edit i ako je false navigiras na home/read stranicu, ili bilo koju koja je dozvoljena
    // a delete posto je dugme njega disable-ujes ako localStorage.currentUser.canDeleteUsers === false
    return true;
  } else {
    // Redirect to the login page
    router.navigate(['/login']);
    return false;
  }
};

export const canActivateRead: CanActivateFn = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot) => {
  const canReadUsers = JSON.parse(sessionStorage.getItem('loggedInUser') || '').canReadUsers; 
  if(!canReadUsers) alert('Not authenticated to read users!');
  return canReadUsers;
}

export const canActivateCreate: CanActivateFn = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot) => {
  const canCreateUsers = JSON.parse(sessionStorage.getItem('loggedInUser') || '').canCreateUsers; 
  if(!canCreateUsers) alert('Not authenticated to create users!');
  return canCreateUsers;
}

export const canActivateUpdate: CanActivateFn = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot) => {
  const canUpdateUsers = JSON.parse(sessionStorage.getItem('loggedInUser') || '').canUpdateUsers; 
  if(!canUpdateUsers) alert('Not authenticated to update users!');
  return canUpdateUsers;
}

// export const canActivateDelete: CanActivateFn = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot) => {
//   const canDeleteUsers = JSON.parse(sessionStorage.getItem('loggedInUser') || '').canDeleteUsers; 
//   if(!canDeleteUsers) alert('Not authenticated to delete users!');
//   return canDeleteUsers;
// }