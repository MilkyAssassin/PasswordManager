import { faker } from "@faker-js/faker";
import type { User, Entry } from "../types/types";

const API_URL = "";
export async function fetchUser(): Promise<User> {
    return new Promise((resolve, reject) => {
        setTimeout(() => {
            resolve(fakeFetchUser()); // Simulate successful response
        }, 1000);
    });
}
function fakeFetchUser(): User {
    let response: User = {
        username: faker.internet.username(),
        vault: [],
    };
    const num_of_entries = Math.floor(Math.random() * (25 - 0 + 1)) + 0;

    for (let i = 1; i <= num_of_entries; i++) {
        const entry: Entry = {
            id: i,
            title: faker.company.name(),
            username: faker.internet.username(),
            url: faker.internet.url(),
            password: faker.internet.password(),
            notes: faker.lorem.text(),
            dateCreated: faker.date
                .between({ from: "2000-01-01", to: "2015-01-01" }),
            lastUsed: faker.date
                .between({ from: "2020-01-01", to: Date.now() }),
        };
        response.vault.push(entry);
    }

    return response;
}
