import { faker } from "@faker-js/faker";
import type { User, Entry } from "@my-types/types";
const API_URL = "";
const LOGIN_ENDPOINT = API_URL + "";
const LOGIN_METHOD = "";
const REGISTER_ENDPOINT = API_URL + "";
const REGISTER_METHOD = "";
const DELETE_ENDPOINT = API_URL + "";
const DELETE_METHOD = "";
const EDIT_ENDPOINT = API_URL + "";
const EDIT_METHOD = "";
const ADD_ENDPOINT = API_URL + "";
const ADD_METHOD = "";

export async function login(data: {
    username: string;
    password: string;
}): Promise<boolean> {
    try {
        const res = await fetch(LOGIN_ENDPOINT, {
            method: LOGIN_METHOD,
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(data),
        });
        if (res.ok) {
            const data = await res.json();
            console.log("Get cookie from fetch");
            console.error("Get cookie from fetch");
            console.log("Get cookie from fetch");
            console.error("Get cookie from fetch");
            console.log("Get cookie from fetch");
            console.error("Get cookie from fetch");

            return true;
        }
    } finally {
        return false;
    }
}

export async function register(data: { username: string; password: string }) {
    try {
        const res = await fetch(REGISTER_ENDPOINT, {
            method: REGISTER_METHOD,
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(data),
        });
        if (res.ok) {
            const data = await res.json();
            console.log("Get cookie from fetch");
            console.error("Get cookie from fetch");
            console.log("Get cookie from fetch");
            console.error("Get cookie from fetch");
            console.log("Get cookie from fetch");
            console.error("Get cookie from fetch");
            return true;
        }
    } finally {
        return false;
    }
}

export async function fetchUser(): Promise<User> {
    return new Promise((resolve, reject) => {
        setTimeout(() => {
            resolve(fakeFetchUser()); // Simulate successful response
        }, 1000);
    });
}

export async function deletePassword(data: { passwordId: number }) {
    try {
        const res = await fetch(DELETE_ENDPOINT, {
            method: DELETE_METHOD,
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(data),
        });
        if (res.ok) {
            const data = await res.json();
            console.log("Get cookie from fetch");
            console.error("Get cookie from fetch");
            console.log("Get cookie from fetch");
            console.error("Get cookie from fetch");
            console.log("Get cookie from fetch");
            console.error("Get cookie from fetch");
            return true;
        }
    } finally {
        return false;
    }
}

export async function addPassword(data:Entry) {
    try {
        const res = await fetch(ADD_ENDPOINT, {
            method: ADD_METHOD,
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(data),
        });
        if (res.ok) {
            const data = await res.json();
            console.log("Get cookie from fetch");
            console.error("Get cookie from fetch");
            console.log("Get cookie from fetch");
            console.error("Get cookie from fetch");
            console.log("Get cookie from fetch");
            console.error("Get cookie from fetch");
            return true;
        }
    } finally {
        return false;
    }

}

export async function editPassword(data:Entry) {
    try {
        const res = await fetch(EDIT_ENDPOINT, {
            method: EDIT_METHOD,
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(data),
        });
        if (res.ok) {
            const data = await res.json();
            console.log("Get cookie from fetch");
            console.error("Get cookie from fetch");
            console.log("Get cookie from fetch");
            console.error("Get cookie from fetch");
            console.log("Get cookie from fetch");
            console.error("Get cookie from fetch");
            return true;
        }
    } finally {
        return false;
    }}

function fakeFetchUser(): User {
    let response: User = {
        username: faker.internet.username(),
        vault: [],
    };
    const num_of_entries = Math.floor(Math.random() * (25 - 0 + 1)) + 0;

    for (let i = 1; i <= num_of_entries; i++) {
        console.log(faker.person.fullName());
        const entry: Entry = {
            id: i,
            title: faker.company.name(),
            username: faker.internet.username(),
            url: faker.internet.url(),
            password: faker.internet.password(),
            notes: faker.lorem.text(),
            dateCreated: faker.date.between({
                from: "2000-01-01",
                to: "2015-01-01",
            }),
            lastUsed: faker.date.between({
                from: "2020-01-01",
                to: Date.now(),
            }),
        };
        response.vault.push(entry);
    }

    return response;
}
