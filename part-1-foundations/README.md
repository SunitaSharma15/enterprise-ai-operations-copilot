# Part 1 — Cost-Optimized Spring AI Foundations

Java 17, Spring Boot 3.5.6, Spring AI 1.1.2, and OpenAI `gpt-4.1-mini`.

## Cost-control design

| Endpoint | Use case | AI/tool behavior |
|---|---|---|
| `POST /api/v1/chat` | General questions | One model request; no tools |
| `POST /api/v1/orders/chat` | Natural-language order question | Tool enabled; may require multiple model requests |
| `GET /api/v1/orders/{id}/status` | Known order ID | Direct Java lookup; zero model tokens |
| `POST /api/v1/incidents/classify` | Structured classification | One concise structured model response |

Other controls include a 250-token output limit, 2,000-character input validation,
short prompts, a short tool description, and a minimal tool response.

## Run in STS

1. Confirm that Windows has `OPENAI_API_KEY` set, and restart STS after setting it.
2. Select **File > Import > Maven > Existing Maven Projects**.
3. Select this `part-1-foundations` directory.
4. Right-click the project and select **Maven > Update Project**.
5. Right-click `Part1Application.java` and select **Run As > Spring Boot App**.
6. Confirm that the console reports port `8081`.

Never place the API key in `application.yml`, source code, Postman, screenshots,
Git commits, or LinkedIn content.

## Run tests before using paid endpoints

From STS, right-click the project and select **Run As > Maven test**.

From a terminal:

```bash
mvn clean test
```

The unit tests do not call OpenAI and therefore incur no API cost.

## Test with Postman

Import `postman/Part-1-Cost-Optimized.postman_collection.json`, then run requests
individually. Test the zero-token direct endpoint first.

Expected direct response:

```json
{
  "orderId": "ORD-1001",
  "status": "SHIPPED"
}
```

Unknown but valid order IDs return HTTP `404`. Malformed IDs return HTTP `400`.

## Production note

The in-memory order map is intentionally small for training. In production,
`OrderStatusService` would call an order database or secured order microservice.
The REST endpoint and AI tool would continue to reuse that same service.
