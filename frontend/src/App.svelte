<script lang="ts">
    import { fetchUser } from "./lib/fetchData.svelte";
    import type { Context } from "@my-types/types";
    import { setContext } from "svelte";
    import Hub from "./lib/Hub.svelte";

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
            context.isAuthed = true;
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
        Not authed
    {/if}
{:else}
    Loading...
{/if}
