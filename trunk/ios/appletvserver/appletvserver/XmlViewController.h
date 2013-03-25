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
}
@property (nonatomic, copy) NSString* xml;
@property (nonatomic, copy) NSString* type;

-(id) initWithXml:(NSString*) xml;
@end
