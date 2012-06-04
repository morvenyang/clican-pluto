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
    
    
    
    int gridx=self.tileMap.contentSizeInPixels.width/self.tileMap.tileSize.width;
    int gridy=self.tileMap.contentSizeInPixels.height/self.tileMap.tileSize.height;
    NSMutableDictionary* mapTypeMetrix = [[[NSMutableDictionary alloc] init] autorelease];
    for(int i=0;i<gridx;i++){
        for(int j=0;j<gridy;j++){
            int tileId=[[self.tileMap layerNamed:@"GameEventLayer"] tileGIDAt:ccp(i, j)];
            NSDictionary* dictionary=[self.tileMap propertiesForGID:tileId];
            if(dictionary){
                NSString* mapTypeStr = [dictionary objectForKey:@"type"];
                Position* position = [Position initWithX:i Y:gridy-j];
                int mapType = [mapTypeStr intValue];
                [mapTypeMetrix setValue:[NSNumber numberWithInt:mapType] forKey:[position description]];
            }
        }
    }
    Position* maxPosi = [Position initWithX:gridx Y:gridy];
    
    CCArray* posiArray = [PositionUtil calcMoveOrbitarrayFromPosition:charPosi movement:5 mobility:moblitity mapTypeMetrix:mapTypeMetrix maxPosition:maxPosi];
    MovementSprite* movementSprite = [MovementSprite initWithPosiArray:posiArray];
    [self addChild:movementSprite];
}
@end
