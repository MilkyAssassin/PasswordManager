<script lang="ts">
    import Vault from "./lib/components/Vault/index.svelte";
    import Sidebar from "./lib/components/Sidebar.svelte";
</script>

<main class="h-dvh grid grid-cols-5">
    <div class="">
        <Sidebar />
    </div>
    <div class="col-span-4">
        <Vault />
    </div>
</main>
