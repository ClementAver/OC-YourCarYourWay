export interface User {
  id: number;
  email: string;
  name: string;
  role: 'EMPLOYEE' | 'CUSTOMER';
}
