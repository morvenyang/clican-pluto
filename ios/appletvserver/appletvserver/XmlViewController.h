//
//  XmlViewController.h
//  appletvserver
//
//  Created by zhang wei on 13-3-25.
//
//

#import <Three20UI/Three20UI.h>

@interface XmlViewController : TTViewController{
    NSString* _xml;
}
@property (nonatomic, copy) NSString* xml;
-(id) initWithXml:(NSString*) xml;
@end
