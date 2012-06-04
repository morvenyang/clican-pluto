//
//  MapLayer.m
//  ccdream
//
//  Created by zhang wei on 12-6-3.
//  Copyright 2012年 Clican. All rights reserved.
//

#import "MapLayer.h"


@implementation MapLayer

@synthesize char1 = _char1;
@synthesize tileMap = _tileMap;

+(CCScene *) scene{
    CCScene* scene = [CCScene node];
    MapLayer* layer = [MapLayer node];
    layer.char1=[[[Character alloc] initWithParentNode:layer spriteFile:@"trs-012.gif"] autorelease];
    [layer.char1.characterSelectDelegateArray addObject:layer];
    [scene addChild:layer];
    return scene;
}

-(id) init{
    self = [super init];
    if(self!=nil){
        self.tileMap = [CCTMXTiledMap tiledMapWithTMXFile:@"ccdream_map01.tmx"];
        [self addChild:self.tileMap z:-1 tag:1]; 
        CCTMXLayer* eventLayer = [self.tileMap layerNamed:@"GameEventLayer"]; 
        eventLayer.visible = YES;
    }
    return self;
}

- (void)selectCharacter:(Character*) character{
    CCSprite* sp = character.characterSprite;
    Position* charPosi = [Position initWithX:sp.position.x Y:sp.position.y];
    Mobility* moblitity = [Mobility initWithDefault];
    NSMutableDictionary* mapTypeMetrix = [NSMutableDictionary dictionary];
    for(int i=0;i<self.tileMap.grid.gridSize.x;i++){
        for(int j=0;j<self.tileMap.grid.gridSize.y;j++){
            int tileId=[[self.tileMap layerNamed:@"GameEventLayer"] tileGIDAt:ccp(i*32, j*32)];
            NSDictionary* dictionary=[self.tileMap propertiesForGID:tileId];
            if(dictionary){
                NSString* mapTypeStr = [dictionary objectForKey:@"mapType"];
                Position* position = [Position initWithX:i Y:j];
                [mapTypeMetrix setObject:[NSNumber numberWithInt:[mapTypeStr intValue]] forKey:position];
            }
        }
    }
    Position* maxPosi = [Position initWithX:self.tileMap.grid.gridSize.x Y:self.tileMap.grid.gridSize.y];
    
    CCArray* posiArray = [PositionUtil calcMoveOrbitarrayFromPosition:charPosi movement:5 mobility:moblitity mapTypeMetrix:mapTypeMetrix maxPosition:maxPosi];
    MovementSprite* movementSprite = [MovementSprite initWithPosiArray:posiArray];
    [self addChild:movementSprite];
}
@end
