<script lang="ts">
    import Vault from "./lib/components/Vault/index.svelte";
    import Sidebar from "./lib/components/Sidebar.svelte";
    import { setContext } from "svelte";
    import type { Context } from "./types/types";
    import { fetchUser } from "./lib/fetchData.svelte";

    let context: Context = $state({
        isLoading: true,
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
