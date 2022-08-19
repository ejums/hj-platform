<script setup lang="ts">
import {ref} from 'vue'

let channelList = ref([]);
async function loadLoginChannel(){
  await fetch("/oauth/channel").then(resp => {
    resp.json().then(list => {
      channelList.value = list;
      console.log(channelList, list);
    })
  })
}
loadLoginChannel();
</script>

<template>
  <div>
    <ul>
      <li v-for="channel in channelList" :key="channel.clientKey">
        <a href="/oauth2/authorization/{{channel.clientKey}}">{{channel.clientName}}</a>
      </li>
    </ul>
  </div>
</template>

<style scoped>
.read-the-docs {
  color: #888;
}
</style>
