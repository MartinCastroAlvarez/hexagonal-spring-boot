// Models.tsx

export interface File {
    id: number | null;
    fileName: string;
    fileHash: string;
}

export interface Meeting {
    id: number | null;
    title: string;
    date: string;
}

export interface Message {
    id: number | null;
    subject: string;
    text: string;
    creationDate: string;
    sentAt: string;
    senderId: number;
}

export interface Product {
    id: number | null;
    name: string;
    isActive: boolean;
    price: number;
}

export interface Pto {
    id: number | null;
    day: string;
    type: string;
    userId: number;
}

export interface Purchase {
    id: number | null;
    cost: number;
    supplierId: number;
}

export interface Sale {
    id: number | null;
    price: number;
    salesmanId: number;
}

export interface Schedule {
    id: number | null;
    startTime: string;
    endTime: string;
    dayOfWeek: number;
    userId: number;
}

export interface Transaction {
    id: number | null;
    amount: number;
    datetime: string;
    productId: number;
}

export interface User {
    id: number | null;
    name: string;
    email: string;
    lastLoginDate: string;
    signUpDate: string;
    isActive: boolean;
    role: string;
}

export interface Work {
    id: number | null;
    startTime: string;
    endTime: string;
    userId: number;
}
