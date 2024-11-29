export type Entry = {
    id: number;
    title: string;
    username: string;
    password: string;
    url: string;
    notes: string;
    dateCreated: Date;
    lastUsed: Date;
}

export interface User {
    username: string;
    vault: Entry[]
}

export interface Context {
    isLoading: boolean;
    user: User|null;
}
