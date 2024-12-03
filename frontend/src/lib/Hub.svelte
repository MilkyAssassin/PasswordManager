<script lang="ts">
    import Vault from "./components/Vault/index.svelte";
    import Sidebar from "./components/Sidebar.svelte";
</script>

<main class="h-dvh grid grid-cols-5">
    <div class="">
        <Sidebar />
    </div>
    <div class="col-span-4">
        <Vault />
    </div>
</main>
