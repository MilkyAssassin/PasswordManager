<script lang="ts">
    import Header from "./Header.svelte";
    import VaultBody from "./VaultBody.svelte";
    let search = $state("")
    let sort: "AlphabeticalOrder"|"AlphabeticalOrderReverse"|"DateCreated"|"DateCreatedReverse" = $state("AlphabeticalOrder")
</script>

<div class="w-full bg-gray-600 h-full">
    <Header bind:search={search} bind:sort={sort} />
    <VaultBody bind:search={search} bind:sort={sort}/>
</div>
