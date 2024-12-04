<script lang="ts">
    import { fetchUser } from "./lib/fetchData.svelte";
    import type { Context } from "@my-types/types";
    import { setContext } from "svelte";
    import Hub from "./lib/Hub.svelte";
    import GetAuthed from "./lib/components/Authenticate/GetAuthed.svelte";

    let context: Context = $state({
        isLoading: true,
        isAuthed: false,
        user: null,
    });
    setContext("context", context);

    async function updateUser() {
        let user = await fetchUser();
        context.isLoading = false;
        if (user != null) {
            context.user = user;
            console.log("TO be able to login replace context.isAuthed with true")
            console.error("TO be able to login replace context.isAuthed with true")
            console.error("TO be able to login replace context.isAuthed with true")
            console.log("TO be able to login replace context.isAuthed with true")
            context.isAuthed = false;
        } else {
            context.user = null;
            context.isAuthed = false;
        }
    }

    $effect(() => {
        updateUser();
    });

</script>

{#if !context.isLoading}
    {#if context.isAuthed}
        <Hub />
    {:else}
        <GetAuthed />
    {/if}
{:else}
    Loading...
{/if}
