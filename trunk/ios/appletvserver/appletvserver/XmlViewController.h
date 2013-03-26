//
//  XmlViewController.h
//  appletvserver
//
//  Created by zhang wei on 13-3-25.
//
//

#import <Three20UI/Three20UI.h>

@interface XmlViewController : TTViewController<UITableViewDelegate>{
    NSString* _xml;
    NSString* _type;
    BOOL _append;
    NSMutableArray* _videos;
}
@property (nonatomic, copy) NSString* xml;
@property (nonatomic, copy) NSString* type;
@property (nonatomic, assign) BOOL append;
@property (nonatomic, retain) NSMutableArray* videos;

-(id) initWithXml:(NSString*) xml;
-(void) appendXml:(NSString*) xml;
@end
