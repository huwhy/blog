package cn.huwhy.katyusha.dao;

import cn.huwhy.common.utils.BeanUtils;
import cn.huwhy.common.utils.StringUtils;
import cn.huwhy.katyusha.enums.ItemStatus;
import cn.huwhy.katyusha.model.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.jfinal.plugin.spring.jdbc.Dao;
import com.jfinal.plugin.spring.jdbc.PageList;
import com.jfinal.plugin.spring.jdbc.PageParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.*;

@Repository
public class ItemDao extends Dao<Item, Integer> {

    @Autowired
    private ItemPicDao itemPicDao;

    @Autowired
    private ItemPropDao itemPropDao;

    @Autowired
    private PropValueDao propValueDao;

    @Autowired
    private ItemCatDao itemCatDao;

    public ItemDao() {
        super("item", Item.class);
    }

    public List<Item> getItems(int size) {
        List<Item> items = findByWhere("status=? order by updated desc limit ?", ItemStatus.display.name(), size);
        for (Item item : items) {
            fullItemInfo(item);
        }
        return items;
    }

    public Item getItem(int id, boolean fullInfo) {
        Item item = getByPK(id);
        if (fullInfo) {
            fullItemInfo(item);
        }
        return item;
    }

    public void display(int id) {
        updateByWhere("status=?", "id=?", ItemStatus.display.name(), id);
    }

    public void hide(int id) {
        updateByWhere("status=?", "id=?", ItemStatus.display.name(), id);
    }

    public void save(Item item, Integer[] picIds, Integer[] colorValueIds, Integer[] sizeValueIds,
                     Collection<ItemPic> pics) throws SQLException {
        if (item.getId() > 0) {
            Item itemDb = getByPK(item.getId());
            BeanUtils.copyProperties(item, itemDb, "id", "created", "status");
            update(itemDb, "updated");
            item = itemDb;
            List<ItemPic> dbPics = itemPicDao.getPicsByIid(item.getId());
            Set<Integer> retainIds = Sets.newHashSet();
            if (picIds != null) {
                retainIds.addAll(Arrays.asList(picIds));
            }
            List<Integer> delIds = Lists.newArrayList();
            for (ItemPic pic : dbPics) {
                if (!retainIds.contains(pic.getId())) {
                    delIds.add(pic.getId());
                }
            }
            if (!delIds.isEmpty()) {
                deleteByIds(delIds.toArray());
            }
        } else {
            Object iid = add(item);
            item.setId(Integer.valueOf(iid.toString()));
        }

        for (ItemPic pic : pics) {
            pic.setItemId(item.getId());
        }
        itemPicDao.adds(pics);

        Set<Integer> valueIds = Sets.newHashSet(colorValueIds);
        valueIds.addAll(Arrays.asList(sizeValueIds));

        List<ItemProp> dbItemProps = itemPropDao.getPropsByIid(item.getId());
        List<Integer> delItemPropIds = Lists.newArrayList();
        Set<Integer> retainValueIds = Sets.newHashSet();
        for (ItemProp prop : dbItemProps) {
            if (!valueIds.contains(prop.getValueId())) {
                delItemPropIds.add(prop.getId());
            } else {
                retainValueIds.add(prop.getValueId());
            }
        }
        List<ItemProp> newItemProps = Lists.newArrayList();
        Map<Integer, PropValue> idColors = propValueDao.findIdValuesByPropId(Prop.COLOR_ID);
        for (Integer valueId : colorValueIds) {
            if (!retainValueIds.contains(valueId)) {
                PropValue pv = idColors.get(valueId);
                if (pv != null) {
                    newItemProps.add(new ItemProp(item.getId(), Prop.COLOR_ID, valueId, pv.getValue()));
                }
            }
        }
        Map<Integer, PropValue> idSizes = propValueDao.findIdValuesByPropId(Prop.SIZE_ID);
        for (Integer valueId : sizeValueIds) {
            if (!retainValueIds.contains(valueId)) {
                PropValue pv = idSizes.get(valueId);
                if (pv != null) {
                    newItemProps.add(new ItemProp(item.getId(), Prop.SIZE_ID, valueId, pv.getValue()));
                }
            }
        }

        itemPropDao.adds(newItemProps);
    }

    private void fullItemInfo(Item item) {
        if (item != null) {
            List<ItemPic> pics = itemPicDao.getPicsByIid(item.getId());
            item.setPicList(pics);
            if (item.getMainPicture() == null && !pics.isEmpty()) {
                item.setMainPicture(pics.get(0).getPath());
            }
            List<ItemProp> props = itemPropDao.getPropsByIid(item.getId());
            if (props != null) {
                List<ItemProp> colorProps = Lists.newArrayList();
                List<ItemProp> sizeProps = Lists.newArrayList();
                for (ItemProp prop : props) {
                    if (prop.getPropId() == Prop.COLOR_ID) {
                        colorProps.add(prop);
                    } else if (prop.getPropId() == Prop.SIZE_ID) {
                        sizeProps.add(prop);
                    }
                }
                item.setSizeProps(sizeProps);
                item.setColorProps(colorProps);
            }
            ItemCat thirdCat = itemCatDao.getByPK(item.getThirdCid());
            if (thirdCat != null) {
                item.setThirdName(thirdCat.getName());
            }
            ItemCat secondCat = itemCatDao.getByPK(item.getSecondCid());
            if (secondCat != null) {
                item.setSecondName(secondCat.getName());
            }
            ItemCat firstCat = itemCatDao.getByPK(item.getFirstCid());
            if (firstCat != null) {
                item.setFirstName(firstCat.getName());
            }
        }
    }

    public List<Integer> findDisplayItemIds() {
        return findSingleList("select id from item where status=?", Integer.class, ItemStatus.display.name());
    }

    public PageList<Item> findItems(int firstCid, int secondCid, int thirdCid, String name, String tag, Long priceStart,
                                    Long priceEnd, ItemStatus status, Date updatedStart, Date updatedEnd, Date createdStart, Date createdEnd,
                                    PageParam pageParam) {
        StringBuilder sql = new StringBuilder();
        List<Object> params = new ArrayList<>();
        sql.append("select it.*, a.name firstName, b.name secondName, c.name thirdName from item it")
                .append(" left join item_cat a on it.first_cid=a.id")
                .append(" left join item_cat b on it.second_cid=b.id")
                .append(" left join item_cat c on it.third_cid=c.id").append(" where 1=1");
        if (firstCid > 0) {
            sql.append(" and it.first_cid=?");
            params.add(firstCid);
        }
        if (secondCid > 0) {
            sql.append(" and it.second_cid=?");
            params.add(secondCid);
        }
        if (thirdCid > 0) {
            sql.append(" and it.third_cid=?");
            params.add(thirdCid);
        }
        if (StringUtils.notBlank(name)) {
            sql.append(" and it.name like ?");
            params.add(name + "%");
        }

        if (StringUtils.notBlank(tag)) {
            sql.append(" and it.name like ?");
            params.add("%" + tag + "%");
        }

        if (priceStart != null) {
            sql.append(" and it.price >= ?");
            params.add(priceStart);
        }
        if (priceEnd != null) {
            sql.append(" and it.price < ?");
            params.add(priceEnd);
        }
        if (status != null && status != ItemStatus.unknown) {
            sql.append(" and it.status =?");
            params.add(status.name());
        }
        if (updatedStart != null) {
            sql.append(" and it.updated >=?");
            params.add(updatedStart);
        }
        if (updatedEnd != null) {
            sql.append(" and it.updated < ?");
            params.add(updatedEnd);
        }
        if (createdStart != null) {
            sql.append(" and it.created >=?");
            params.add(createdStart);
        }
        if (createdEnd != null) {
            sql.append(" and it.created <?");
            params.add(createdEnd);
        }

        return findPaging(sql, pageParam, params);
    }

}
