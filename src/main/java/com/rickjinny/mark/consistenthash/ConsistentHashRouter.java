package com.rickjinny.mark.consistenthash;

import java.security.MessageDigest;
import java.util.Collection;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

public class ConsistentHashRouter<T extends Node> {

    private final SortedMap<Long, VirtualNode<T>> ring = new TreeMap<>();

    private final HashFunction hashFunction;

    public ConsistentHashRouter(Collection<T> pNodes, int vNodeCount) {
        this(pNodes, vNodeCount, new MD5Hash());
    }

    public ConsistentHashRouter(Collection<T> pNodes, int vNodeCount, HashFunction hashFunction) {
        if (hashFunction == null) {
            throw new NullPointerException("Hash Function is null");
        }
        this.hashFunction = hashFunction;
        if (pNodes != null) {
            for (T pNode : pNodes) {
                addNode(pNode, vNodeCount);
            }
        }
    }

    private void addNode(T pNode, int vNodeCount) {
        if (vNodeCount < 0) {
            throw new IllegalArgumentException("illegal virtual node counts : " + vNodeCount);
        }

        int existingReplicas = getExistingReplicas(pNode);
        for (int i = 0; i < vNodeCount; i++) {
            VirtualNode<T> vNode = new VirtualNode<>(pNode, i + existingReplicas);
            ring.put(hashFunction.hash(vNode.getKey()), vNode);
        }
    }

    public void removeNode(T pNode) {
        Iterator<Long> iterator = ring.keySet().iterator();
        while (iterator.hasNext()) {
            Long key = iterator.next();
            VirtualNode<T> virtualNode = ring.get(key);
            if (virtualNode.isVirtualNodeOf(pNode)) {
                iterator.remove();
            }
        }
    }

    public T routNode(String objectKey) {
        if (ring.isEmpty()) {
            return null;
        }
        long hashValue = hashFunction.hash(objectKey);
        SortedMap<Long, VirtualNode<T>> tailSortedMap = ring.tailMap(hashValue);
        Long nodeHashValue = !tailSortedMap.isEmpty() ? tailSortedMap.firstKey() : ring.firstKey();
        return ring.get(nodeHashValue).getPhysicalNode();
    }

    private int getExistingReplicas(T pNode) {
        int replicas = 0;
        for (VirtualNode<T> vNode : ring.values()) {
            if (vNode.isVirtualNodeOf(pNode)) {
                replicas++;
            }
        }
        return replicas;
    }

    private static class MD5Hash implements HashFunction {

        MessageDigest messageDigest;

        public MD5Hash() {
            try {
                messageDigest = MessageDigest.getInstance("MD5");
            } catch (Exception e) {
                //
            }
        }

        @Override
        public long hash(String key) {
            messageDigest.reset();
            messageDigest.update(key.getBytes());
            byte[] digest = messageDigest.digest();

            long h = 0;
            for (int i = 0; i < 4; i++) {
                h <<= 8;
                h |= (int) digest[i] & 0xFF;
            }
            return h;
        }
    }
}

