import { Injectable } from '@angular/core';
import * as jwt_decode from "jwt-decode";


export function decodeToken(token: string) {
    try {
        return jwt_decode.jwtDecode(token);
    } catch (error) {
        console.error('Error decoding JWT:', error);
        return null;
    }
}

export function tokenExpired(token?: string) {
    if(!token) return false;
    const expiry = (JSON.parse(atob(token.split('.')[1]))).exp;
    console.log('expiry', expiry);
    
    return (Math.floor((new Date).getTime() / 1000)) >= expiry;
}
