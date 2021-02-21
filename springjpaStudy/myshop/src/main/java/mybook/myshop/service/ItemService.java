package mybook.myshop.service;

import lombok.RequiredArgsConstructor;
import mybook.myshop.domain.item.Item;
import mybook.myshop.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }


    // merge 가 될때 일어나는 메서드를 구현해놓은 것
    @Transactional
    public Item updateItem(Long itemId, String name, int price, int stockQuantity) {
        Item findItem = itemRepository.findOne(itemId);
        findItem.setPrice(price);
        findItem.setName(name);
        findItem.setStockQuantity(stockQuantity);

        // 보통 업데이트는 ... 업데이트 하는 메서드가 따로 있어야한다.
        /// ex
        // findItem.change(price, name, stockQuantity);
        // findItem.addStock(~~)

        // commit 되면 flush를 날림
        // 그럼 db에 업데이트되는데 이게 변경관리임
        return findItem;
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

}
