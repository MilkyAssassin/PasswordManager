<script lang="ts">
    import Vault from "./components/Vault/index.svelte";
    import Sidebar from "./components/Sidebar.svelte";
    import { setContext } from "svelte";
    import type { Context } from "@my-types/types";
    import { fetchUser } from "./fetchData.svelte";

    let context: Context = $state({
        isLoading: true,
        isAuthed: false,
        user: null,
    });

    setContext("context", context);

    async function updateUser() {
        let user = await fetchUser();
        context.isLoading = false;
        context.user = user;
    }

    $effect(() => {
        updateUser();
    });
</script>

<main class="h-dvh grid grid-cols-5">
    <div class="">
        <Sidebar />
    </div>
    <div class="col-span-4">
        <Vault />
    </div>
</main>
