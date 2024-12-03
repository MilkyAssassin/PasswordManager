<script lang="ts">
    import Login from "./Login.svelte";
    import Register from "./Register.svelte";

    let isPageLogin = $state(true);
</script>

<div class="h-dvh bg-[#d8cbea] p-10">
    <div class="w-5/6 bg-white rounded-lg mx-auto">
        {#if isPageLogin}
        <Login/>
        {:else}
        <Register />
        {/if}
        Login 
    </div>
</div>
