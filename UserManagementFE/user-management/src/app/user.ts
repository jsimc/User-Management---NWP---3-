export interface User {
    username: string;
    email: string;
    password: string;
    canCreateUsers: boolean;
    canReadUsers: boolean;
    canUpdateUsers: boolean;
    canDeleteUsers: boolean;
}