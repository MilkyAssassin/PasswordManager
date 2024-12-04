<script lang="ts">
    import { login, register } from "../../fetchData.svelte";
    import Login from "./Login.svelte";
    import { notifications } from "../Toast/notifications.svelte";
    import Register from "./Register.svelte";
    let isPageLogin = $state(true);

    let submit = async (
        e: SubmitEvent & {
            currentTarget: EventTarget & HTMLFormElement;
        },
        username: string,
        password: string,
        email: string
    ) => {
        e.preventDefault();
        let isLoginSuccessful = false;
        if (isPageLogin) {
            isLoginSuccessful = await login({
                username,
                password,
            });
        } else {
            isLoginSuccessful = await register({
                username,
                password,
                email,
            });
        }

        if (isLoginSuccessful) {
            window.location.reload();
        } else {
            notifications.danger(
                isPageLogin
                    ? "Username/Password combination is incorrect"
                    : "Registration failed :(",
                3000
            );
        }
    };
</script>

<div class="h-dvh bg-[#d8cbea] p-10 overflow-scroll">
    <div class="w-5/6 max-w-[400px] bg-white rounded-lg mx-auto p-14">
        <h1 class="text-2xl text-center">
            {isPageLogin ? "Login" : "Register"}
        </h1>

        {#if isPageLogin}
            <Login {submit} />
        {:else}
            <Register {submit} />
        {/if}

        <div class="text-center pt-4">
            {#if isPageLogin}
                Don't have an account? <button
                    class="text-blue-700"
                    onclick={() => (isPageLogin = false)}>Register</button
                >
            {:else}
                Have an account? <button
                    class="text-blue-700"
                    onclick={() => (isPageLogin = true)}>Login</button
                >
            {/if}
        </div>
    </div>
</div>
