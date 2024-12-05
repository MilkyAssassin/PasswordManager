import { faker } from "@faker-js/faker";
import type { User, Entry } from "@my-types/types";
import type { forEach } from "lodash";
// const API_URL = "https://pokeapi.co/api/v2/pokemon/ditto";
const API_URL = "http://localhost:8080";
const LOGIN_ENDPOINT = API_URL + "/login";
const LOGIN_METHOD = "POST";

export async function login(body: {
    username: string;
    password: string;
}): Promise<boolean> {
    if (body.username == "test" && body.password == "test") {
        localStorage.setItem("authed", "true");
        return true;
    }
    let returnValue = false;
    try {
        const res = await fetch(LOGIN_ENDPOINT, {
            method: LOGIN_METHOD,
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(body),
        });
        if (res.ok) {
            const data = await res.json();
            localStorage.setItem("data", JSON.stringify(data));
            localStorage.setItem("authed", "true");
            localStorage.setItem("id", data.userId);
            localStorage.setItem("uname", data.username || "Unknown");
            return true;
        }
    } catch {
        returnValue = false;
    } finally {
        return returnValue;
    }
}

export async function logout() {
    localStorage.removeItem("data");
    localStorage.removeItem("authed");
    localStorage.removeItem("id");
}

export async function register(body: {
    username: string;
    email: string;
    password: string;
}): Promise<boolean> {
    let returnValue = false;
    try {
        const res = await fetch("http://localhost:8080/register", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(body),
        });
        if (res.ok) {
            const data = await res.json();
            localStorage.setItem("data", JSON.stringify(data));
            localStorage.setItem("authed", "true");
            localStorage.setItem("id", data.userId);
            returnValue = true;
            login({ username: body.username, password: body.password });
        }
    } catch {
        returnValue = false;
    } finally {
        return returnValue;
    }
}

export async function fetchUser(): Promise<User | null> {
    let returnValue = null;
    try {
        const id = localStorage.getItem("id");
        const uname = localStorage.getItem("uname");
        if (id == null || uname == null) {
            return null;
        }
        const res = await fetch("http://localhost:8080/passwords/user/" + id, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
            },
        });
        if (res.ok) {
            const data = await res.json();
            const user: User = {
                username: uname,
                vault: [],
            };
            data.forEach(
                (e: {
                    userID: number;
                    username: string;
                    plainPassword: string;
                    website: string;
                    securityQuestion: string;
                }) => {
                    user.vault.push({
                        id: (e.userID | -1) as number,
                        password: e.plainPassword as string,
                        username: e.username as string,
                        url: e.website as string,
                        notes: e.securityQuestion as string,
                        title: e.website,
                        dateCreated: new Date(Date.now()),
                        lastUsed: new Date(Date.now()),
                    } as Entry);
                }
            );
            returnValue = user;
        }
    } catch {
        returnValue = null;
    } finally {
        return returnValue;
    }
}

export async function deletePassword(body: {
    passwordUrl: string;
}): Promise<boolean> {
    let returnValue = false;
    const id = localStorage.getItem("id");
    if (id === undefined) {
        return false;
    }

    try {
        const res = await fetch(
            "http://localhost:8080/passwords/delete/" +
                id +
                "/" +
                body.passwordUrl,
            {
                method: "DELETE",
                headers: {
                    "Content-Type": "application/json",
                },
            }
        );
        if (res.ok) {
            const data = await res.json();
            returnValue = true;
        }
    } catch {
        returnValue = false;
    } finally {
        return returnValue;
    }
}

export async function addPassword(body: Entry): Promise<boolean> {
    let returnValue = false;
    const id = localStorage.getItem("id");
    if (id === undefined) {
        return false;
    }
    let newBody = {
        userId: id,
        website: body.url,
        username: body.username,
        password: body.password,
        newSecurityQuestion: body.notes,
    };
    try {
        const res = await fetch("http://localhost:8080/passwords/add", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(newBody),
        });
        if (res.ok) {
            await res.json();
            returnValue = true;
        }
    } catch {
        returnValue = false;
    } finally {
        return returnValue;
    }
}

export async function editPassword(
    body: Entry,
    oldWebsite: string
): Promise<boolean> {
    let returnValue = false;
    const id = localStorage.getItem("id");
    if (id === undefined) {
        return false;
    }

    let newBody = {
        userId: id,
        oldWebsite: oldWebsite || "",
        website: body.url || "",
        username: body.username || "",
        password: body.password || "",
        SecurityQuestion: body.notes || "",
    };

    try {
        const res = await fetch(
            "http://localhost:8080/passwords/edit/" + oldWebsite,
            {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(newBody),
            }
        );
        if (res.ok) {
            const data = await res.json();
            returnValue = true;
        }
    } catch {
        returnValue = false;
    } finally {
        return returnValue;
    }
}

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
